package com.optimagrowth.organization.events.source;

import com.optimagrowth.organization.utils.UserContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SimpleSourceBean {

    private final Source source;

    public void publishOrganizationChange(ActionEnum action, String organizationId) {
        log.debug("Sending Kafka message {} for Organization Id: {}", action, organizationId);
        OrganizationChangeModel organizationChangeModel = OrganizationChangeModel.builder()
                .type(OrganizationChangeModel.class.getTypeName())
                .action(action.name())
                .organizationId(organizationId)
                .correlationId(UserContextHolder.getContext().getCorrelationId())
                .build();
        source.output().send(MessageBuilder.withPayload(organizationChangeModel).build());
    }

}
