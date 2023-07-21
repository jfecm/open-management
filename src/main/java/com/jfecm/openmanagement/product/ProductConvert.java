package com.jfecm.openmanagement.product;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductConvert {
    private final ModelMapper modelMapper;

    public ProductConvert(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Product toEntity(ProductRequest productRequest) {
        return modelMapper.map(productRequest, Product.class);
    }
    public ProductResponse ToResponse(Product product) {
        return modelMapper.map(product, ProductResponse.class);
    }

    public void toUpdate(Long id, ProductRequest product, Product toUpdate) {
        toUpdate.setId(id);
        modelMapper.map(product, toUpdate);
    }
}
