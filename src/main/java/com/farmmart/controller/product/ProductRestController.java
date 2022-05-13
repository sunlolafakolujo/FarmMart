package com.farmmart.controller.productrestcontroller;

import com.farmmart.data.model.category.Category;
import com.farmmart.data.model.category.CategoryNotFoundException;
import com.farmmart.data.model.product.*;
import com.farmmart.data.model.vendor.Vendor;
import com.farmmart.data.model.vendor.VendorNotFoundException;
import com.farmmart.service.category.CategoryServiceImp;
import com.farmmart.service.product.ProductServiceImpl;
import com.farmmart.service.vendor.VendorServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/product")
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductServiceImpl productService;

    private final VendorServiceImpl vendorService;

    private final CategoryServiceImp categoryServiceImp;

    private final ModelMapper modelMapper;

    @PostMapping("/saveProduct")
    @PreAuthorize("hasAnyAuthority('ROLE_VENDOR')")
    public ResponseEntity<NewProduct> saveProduct(@RequestBody NewProduct newProduct){
        Product product=modelMapper.map(newProduct, Product.class);

        Product saveProduct=productService.saveProduct(product);

        NewProduct postSave=modelMapper.map(saveProduct, NewProduct.class);

        return new ResponseEntity<>(postSave, HttpStatus.CREATED);
    }

    @GetMapping("/findProductById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ProductDto> getProductById(@PathVariable(value = "id") Long id) throws ProductNotFoundException {

        Product product=productService.findProductById(id);

        ProductDto productDto=convertProductToDto(product);

        return ResponseEntity.ok().body(productDto);
    }

    @GetMapping("/findProductByName/{name}")
    public ResponseEntity<List<ProductDto>> getProductByName(@PathVariable(value = "name") String productName) throws ProductNotFoundException {

        return ResponseEntity.ok().body(productService.findProductByName(productName)
                .stream()
                .map(this::convertProductToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findProductByType/{type}")
    public ResponseEntity<List<ProductDto>> getProductByType(@PathVariable(value = "type") String productType) throws ProductNotFoundException {
        return ResponseEntity.ok().body(productService.findProductByType(productType)
                .stream()
                .map(this::convertProductToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findProductByBrand/{brand}")
    public ResponseEntity<List<ProductDto>> getProductByBrand(@PathVariable(value = "brand") String productBrand) throws ProductNotFoundException {

        return ResponseEntity.ok().body(productService.findProductByBrand(productBrand)
                .stream()
                .map(this::convertProductToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findProductByPriceLessThan/{price}")
    public ResponseEntity<List<ProductDto>> getProductPriceLessThanOrEqualsTo(@PathVariable(value = "price")BigDecimal price) throws ProductNotFoundException {

        return ResponseEntity.ok().body(productService.findProductByPriceLessThanOrEquals(price)
                .stream()
                .map(this::convertProductToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findProductByPriceGreaterThan/{price}")
    public ResponseEntity<List<ProductDto>> getProductPriceGreaterThanOrEqualsTo(@PathVariable(value = "price") BigDecimal price) throws ProductNotFoundException {

        return ResponseEntity.ok().body(productService.findProductByPriceGreaterThanOrEquals(price)
                .stream()
                .map(this::convertProductToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findProductByPriceRange/{minPrice}, {maxPrice}")
    public ResponseEntity<List<ProductDto>> getProductByPriceRange(@PathVariable(value = "minPrice") BigDecimal minPrice,
                                                                   @PathVariable(value = "maxPrice") BigDecimal maxPrice)
                                                                   throws ProductNotFoundException {

        return ResponseEntity.ok().body(productService.findProductByPriceRange(minPrice, maxPrice)
                .stream()
                .map(this::convertProductToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findProductByPartNumber/{partNo}")
    public ResponseEntity<List<ProductDto>> getProductByPartNumber(@PathVariable(value = "partNo") String partNumber) throws ProductNotFoundException {

        return ResponseEntity.ok().body(productService.findProductByPartNumber(partNumber)
                .stream()
                .map(this::convertProductToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findProductByCategory/{category}")
    public ResponseEntity<List<ProductDto>> getProductByCategory(@PathVariable(value = "category") String productCategory) throws CategoryNotFoundException {
        Category category =categoryServiceImp.findCategoryByName(productCategory);

        return ResponseEntity.ok().body(productService.findProductByCategory(category,productCategory)
                .stream()
                .map(this::convertProductToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findProductByVendor/{vendor}")
    public ResponseEntity<List<ProductDto>> getProductByVendor(@PathVariable(value = "vendor") String vendorName) throws VendorNotFoundException {

        Vendor vendor=vendorService.findVendorByName(vendorName);

        return ResponseEntity.ok().body(productService.findProductByVendor(vendor, vendorName)
                .stream()
                .map(this::convertProductToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findAllProducts")
    public ResponseEntity<List<ProductDto>> getAllProducts(){

        return ResponseEntity.ok().body(productService.findAllProduct()
                .stream()
                .map(this::convertProductToDto)
                .collect(Collectors.toList()));
    }

    @PutMapping("/updateProductById/{id}")
    @PreAuthorize("hasAuthority('ROLE_VENDOR')")
    public ResponseEntity<ModifyProduct> updateProduct(@Valid @RequestBody ModifyProduct modifyProduct,
                                                       @PathVariable(value = "id") Long id) throws ProductNotFoundException {

        Product product=modelMapper.map(modifyProduct, Product.class);

        Product updatedProduct=productService.updateProduct(product, id);

        ModifyProduct postUpdate=modelMapper.map(updatedProduct, ModifyProduct.class);

        return ResponseEntity.ok().body(postUpdate);
    }

    @DeleteMapping("deleteProductById/{id}")
    @PreAuthorize("hasAuthority('ROLE_VENDOR')")
    public ResponseEntity<?> deleteProductById(@PathVariable(value = "id") Long id) throws ProductNotFoundException {

        productService.deleteProductById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAllProducts")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAllProducts(){
        productService.deleteAllProduct();

        return ResponseEntity.noContent().build();
    }

    private ProductDto convertProductToDto(Product product){

        ProductDto productDto=new ProductDto();

        productDto.setId(product.getId());
        productDto.setProductType(product.getProductType());
        productDto.setProductName(product.getProductName());
        productDto.setProductDescription(product.getProductDescription());
        productDto.setStockQuantity(product.getStockQuantity());
        productDto.setPrice(product.getPrice());
        productDto.setWeight(product.getWeight());
        productDto.setBrand(product.getBrand());
        productDto.setProductStyle(product.getProductStyle());
        productDto.setProductDimension(product.getProductDimension());
        productDto.setPartNumber(product.getPartNumber());
        productDto.setProductSKU(product.getProductSKU());
        productDto.setProductCondition(product.getProductCondition());
        productDto.setUnitOfMeasure(product.getUnitOfMeasure());
        productDto.setProductAvailability(product.getProductAvailability());
        productDto.setCategoryName(product.getCategory().getCategoryName());
        productDto.setColours(product.getColours());
        productDto.setVendorName(product.getVendor().getName());

        return productDto;

    }
}
