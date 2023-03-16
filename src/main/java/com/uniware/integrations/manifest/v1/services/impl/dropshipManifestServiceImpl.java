package com.uniware.integrations.manifest.v1.services.impl;

import com.unicommerce.platform.aws.S3Service;
import com.unicommerce.platform.integration.ScriptErrorCode;
import com.unicommerce.platform.integration.manifest.models.response.FetchCurrentManifestResponse;
import com.uniware.integrations.client.annotation.FlipkartClient;
import com.uniware.integrations.client.constants.ChannelSource;
import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.uniware.integrations.client.dto.api.requestDto.GetManifestRequest;
import com.uniware.integrations.flipkart.services.FlipkartHelperService;
import com.uniware.integrations.flipkart.services.FlipkartSellerApiService;
import com.uniware.integrations.manifest.v1.services.IManifestService;
import com.unicommerce.platform.integration.Error;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.uniware.integrations.client.constants.flipkartConstants.BUCKET_NAME;
import static com.uniware.integrations.client.constants.flipkartConstants.CURRENT_CHANNEL_MANIFEST;
import static com.uniware.integrations.client.constants.flipkartConstants.PDF;

@Service
@FlipkartClient(module = "MANIFEST", version = "v1", channelSource = ChannelSource.FLIPKART_DROPSHIP)
public class dropshipManifestServiceImpl implements IManifestService {

    @Autowired private FlipkartHelperService flipkartHelperService;
    @Autowired private FlipkartSellerApiService flipkartSellerApiService;
    private S3Service s3Service;
    @Override
    public FetchCurrentManifestResponse fetchCurrentManifest(String shippingProviderCode) {
        FetchCurrentManifestResponse fetchCurrentManifestResponse = new FetchCurrentManifestResponse();

        GetManifestRequest getManifestRequest = new GetManifestRequest.Builder()
                .setParams(new GetManifestRequest.Params.Builder()
                        .setVendorGroupCode(getVendorGroupCode(shippingProviderCode))
                        .setIsMps(false)
                        .setLocationId(FlipkartRequestContext.current().getLocationId())
                        .build())
                .build();

        String manifestFilePath = flipkartHelperService.getFilePath(CURRENT_CHANNEL_MANIFEST,PDF);

        boolean isManifestDownloaded = flipkartSellerApiService.getCurrentChannelManifest(getManifestRequest, manifestFilePath);

        if ( !isManifestDownloaded) {
            fetchCurrentManifestResponse.addError(new Error(ScriptErrorCode.CHANNEL_ERROR.name(),"Unable to download manifest"));
            fetchCurrentManifestResponse.setSuccessful(false);
            return fetchCurrentManifestResponse;
        }

        String manifestLink = s3Service.uploadFile(new File(manifestFilePath),BUCKET_NAME);
        fetchCurrentManifestResponse.setManifestLink(manifestLink);
        fetchCurrentManifestResponse.setSuccessful(true);
        return fetchCurrentManifestResponse;

    }

    private String getVendorGroupCode(String shippingProviderCode) {

        String vendorGroupCode = shippingProviderCode;
        if ( shippingProviderCode.contains("Delhivery") )
            vendorGroupCode = "Delhivery";
        else if ( shippingProviderCode.contains("E-Kart Logistics"))
            vendorGroupCode = "Ekart Logistics";
        else if ( shippingProviderCode.contains("Ecom"))
            vendorGroupCode = "Ecom Express";

        return vendorGroupCode;
    }

}
