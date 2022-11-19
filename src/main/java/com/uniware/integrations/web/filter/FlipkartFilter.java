package com.uniware.integrations.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniware.integrations.client.constants.ChannelSource;
import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.uniware.integrations.client.utils.ThreadContextUtils;
import com.unifier.core.utils.StringUtils;
import com.uniware.integrations.utils.WebUtil;
import com.uniware.integrations.web.context.TenantRequestContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Created by admin on 20/05/22.
 */

@Component
@Order(2)
public class FlipkartFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlipkartFilter.class);
    public static final String API_VERSION = "ApiVersion";

    @Autowired
    public ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws
            ServletException,
            IOException
    {

        try {
            final long startTime = System.currentTimeMillis();
            String requestData = WebUtil.httptoString(request);

            LOGGER.info("Starting processing request :- {}", requestData);

            FlipkartRequestContext.current().setRequestIdentifier(FlipkartRequestContext.generateRequestIdentifier());
            FlipkartRequestContext.current().setChannelSource(
                    ChannelSource.findByChannelSourceCode(TenantRequestContext.current().getSourceCode()));
            String requestURI = WebUtil.getRequestURI(request);
            FlipkartRequestContext.current().setRequestURI(requestURI);

            String apiVersion = request.getHeader(API_VERSION);
            if (StringUtils.isBlank(apiVersion))
                apiVersion = "v1";
            FlipkartRequestContext.current().setApiVersion(apiVersion);
            ThreadContextUtils.setThreadMetadata("HTTP");

            FlipkartRequestContext.current().setAuthToken(request.getHeader("authToken"));
            FlipkartRequestContext.current().setLocationId(request.getHeader("locationId"));
            FlipkartRequestContext.current().setUserName(request.getHeader("userName"));
            FlipkartRequestContext.current().setPassword(request.getHeader("password"));

//            if (!FlipkartRequestContext.current().validate()) {
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
//                return;
//            }
            String hostname = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("hostname").getInputStream())).readLine();
            response.addHeader("Hostname", hostname);
            response.addHeader("RequestIdentifier", FlipkartRequestContext.current().getRequestIdentifier());
            filterChain.doFilter(request, response);
            LOGGER.info("Finished processing request :- {}, time taken : {} ", requestData, System.currentTimeMillis() - startTime);

        } finally {
            FlipkartRequestContext.destroy();
        }
    }

}
