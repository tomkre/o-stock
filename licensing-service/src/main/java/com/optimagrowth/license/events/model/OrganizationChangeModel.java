package com.optimagrowth.license.events.model;

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
