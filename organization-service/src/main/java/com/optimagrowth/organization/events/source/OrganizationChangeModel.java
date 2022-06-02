package com.optimagrowth.organization.events.source;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class OrganizationChangeModel {

    private String type;

    private String action;

    private String organizationId;

    private String correlationId;

}
