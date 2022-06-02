package com.optimagrowth.license.events.handler;

import com.optimagrowth.license.events.CustomChannels;
import com.optimagrowth.license.events.model.OrganizationChangeModel;
import com.optimagrowth.license.repository.OrganizationRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(CustomChannels.class)
@RequiredArgsConstructor
@Slf4j
public class OrganizationChangeHandler {

    private final OrganizationRedisRepository organizationRedisRepository;

    @StreamListener("inboundOrgChanges")
    public void loggerSink(OrganizationChangeModel organizationEvent) {
        log.debug("Received a message of type " + organizationEvent.getType());
        log.debug("Received a message with an event {} from the organization service for the organization id {} ",
            organizationEvent.getAction(), organizationEvent.getOrganizationId());
    }

}
