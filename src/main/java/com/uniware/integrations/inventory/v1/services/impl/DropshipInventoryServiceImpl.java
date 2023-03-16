package com.uniware.integrations.inventory.v1.services.impl;

import com.unicommerce.platform.integration.NameValuePair;
import com.unicommerce.platform.integration.inventory.models.request.UpdateInventoryRequest;
import com.unicommerce.platform.integration.inventory.models.response.UpdateInventoryResponse;
import com.uniware.integrations.client.annotation.FlipkartClient;
import com.uniware.integrations.client.constants.ChannelSource;
import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.uniware.integrations.client.dto.api.requestDto.UpdateInventoryV3Request;
import com.uniware.integrations.client.dto.api.responseDto.UpdateInventoryV3Response;
import com.uniware.integrations.flipkart.services.FlipkartSellerApiService;
import com.unicommerce.platform.integration.Error;
import com.uniware.integrations.inventory.v1.services.IInventoryService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.uniware.integrations.client.constants.flipkartConstants.FAILED;

@Service
@FlipkartClient(module = "INVENTORY", version = "v1", channelSource = ChannelSource.FLIPKART_DROPSHIP)
public class DropshipInventoryServiceImpl implements IInventoryService {

    @Autowired
    private FlipkartSellerApiService flipkartSellerApiService;
    private static final Logger LOGGER = LoggerFactory.getLogger(DropshipInventoryServiceImpl.class);

    private static final List<String> disabledInventoryErrorMessageList = new ArrayList<>();

    static  {
        disabledInventoryErrorMessageList.add("Invalid location provided");
    }

    // TODO : Response builder pattern
    @Override
    public UpdateInventoryResponse updateInventory(UpdateInventoryRequest updateInventoryRequest) {

        UpdateInventoryV3Request updateInventoryV3Request = prepareFlipkartInventoryUpdateRequest(updateInventoryRequest);
        UpdateInventoryV3Response updateInventoryV3Response = flipkartSellerApiService.updateInventory(updateInventoryV3Request);
        Map<String,String> channelSkuCodeToChannelProductId = updateInventoryRequest.getListings().stream().collect(
                Collectors.toMap(UpdateInventoryRequest.Listing::getChannelSkuCode, UpdateInventoryRequest.Listing::getChannelProductId));
        UpdateInventoryResponse updateInventoryResponse = new UpdateInventoryResponse();
        for (Map.Entry<String, UpdateInventoryV3Response.InventoryUpdateStatus> entry: updateInventoryV3Response.getSkus().entrySet()) {
            String channelSkuCode = entry.getKey();
            UpdateInventoryV3Response.InventoryUpdateStatus inventoryUpdateStatus = entry.getValue();
            UpdateInventoryResponse.Listing listing = new UpdateInventoryResponse.Listing();
            if (channelSkuCodeToChannelProductId.containsKey(channelSkuCode)) {
                if ( ("success").equalsIgnoreCase(inventoryUpdateStatus.getStatus())) {
                    listing.setChannelProductId(channelSkuCodeToChannelProductId.get(channelSkuCode));
                    listing.setChannelSkuCode(channelSkuCode);
                    listing.setStatus(UpdateInventoryResponse.Status.SUCCESS);
                }
                else if ( ("failure").equalsIgnoreCase(inventoryUpdateStatus.getStatus()) || ("warning").equalsIgnoreCase(inventoryUpdateStatus.getStatus()) ) {
                    listing.setChannelProductId(channelSkuCodeToChannelProductId.get(channelSkuCode));
                    listing.setChannelSkuCode(channelSkuCode);
                    listing.setStatus(UpdateInventoryResponse.Status.FAILED);
                    if (inventoryUpdateStatus.getErrors() != null)
                        inventoryUpdateStatus.getErrors().stream().forEach(error -> {
                            listing.addError(new Error(error.getCode(), error.getDescription()));
                            if (disabledInventoryErrorMessageList.stream().anyMatch(error.getDescription()::contains))
                                listing.setStatus(UpdateInventoryResponse.Status.DISABLED);
                        });
                    if (inventoryUpdateStatus.getAttributeErrors() != null)
                        inventoryUpdateStatus.getAttributeErrors().stream().forEach(error -> {
                            listing.addError(new Error(error.getCode(), error.getDescription()));
                            if (disabledInventoryErrorMessageList.stream().anyMatch(error.getDescription()::contains))
                                listing.setStatus(UpdateInventoryResponse.Status.DISABLED);
                        });
                }
            }
            updateInventoryResponse.addListing(listing);
            channelSkuCodeToChannelProductId.remove(channelSkuCode);
        }
        channelSkuCodeToChannelProductId.entrySet().stream().forEach(entry -> {
            UpdateInventoryResponse.Listing listing = new UpdateInventoryResponse.Listing();
            listing.addError(new Error(FAILED,"Not getting update confirmation in api response from Flipkart"));
            listing.setChannelSkuCode(entry.getKey());
            listing.setChannelProductId(entry.getValue());
            updateInventoryResponse.addListing(listing);
        });

        updateInventoryResponse.setSuccessful(true);
        return updateInventoryResponse;
    }

    private UpdateInventoryV3Request prepareFlipkartInventoryUpdateRequest(UpdateInventoryRequest updateInventoryRequest) {

        UpdateInventoryV3Request updateInventoryV3Request = new UpdateInventoryV3Request();

        for (UpdateInventoryRequest.Listing listing : updateInventoryRequest.getListings()) {
            UpdateInventoryV3Request.Location location = new UpdateInventoryV3Request.Location();
            location.setId(FlipkartRequestContext.current().getLocationId());
            location.setInventory(listing.getQuantity());

            UpdateInventoryV3Request.SkuDetails skuDetails = new UpdateInventoryV3Request.SkuDetails();
            skuDetails.setProductId(getProductIdForInventoryUpdate(listing.getChannelProductId(), listing.getAttributes()));
            skuDetails.addLocation(location);
            updateInventoryV3Request.addSku(listing.getChannelSkuCode(), skuDetails);
        }
        return updateInventoryV3Request;
    }

    private String getProductIdForInventoryUpdate(String channelProductId, List<NameValuePair> attributes) {

        String fsn;
        Optional<NameValuePair> attribute = attributes == null ? null : attributes.stream().filter(lineItem -> "fsn".equalsIgnoreCase(lineItem.getName())).findFirst();

        if (attribute != null && attribute.isPresent()) {
            fsn = attribute.get().getValue();
        } else {
            fsn = channelProductId.substring(3, channelProductId.length() - 6);
        }

        return fsn;
    }

}
