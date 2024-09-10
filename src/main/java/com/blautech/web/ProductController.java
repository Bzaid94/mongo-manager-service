package com.blautech.web;

import com.blautech.bean.ResponseWS;
import com.blautech.mongodb.model.Product;
import com.blautech.service.ProductManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/product")
public class ProductController {
    private final ProductManager productManager;

    public ProductController(ProductManager productManager) {
        this.productManager = productManager;
    }

    @PostMapping("/create")
    private ResponseEntity<ResponseWS> createProduct(@RequestBody Product product){
        return ResponseEntity.ok(productManager.createProduct(product));
    }

    @GetMapping("/list-products")
    private ResponseEntity<ResponseWS> getAllProduct(){
        return ResponseEntity.ok(productManager.getAllProducts());
    }

    @GetMapping({"/{id}"})
    private ResponseEntity<ResponseWS> getProduct(@PathVariable String id){
        return ResponseEntity.ok(productManager.getProductBy(id));
    }

    @PutMapping("/update/{id}")
    private ResponseEntity<ResponseWS> updateProduct(@PathVariable String id, @RequestBody Product updatedProduct){
        return ResponseEntity.ok(productManager.updateProduct(id, updatedProduct));
    }

    @PutMapping("/delete/{id}")
    private ResponseEntity<ResponseWS> deleteProduct(@PathVariable String id){
        return ResponseEntity.ok(productManager.deleteProduct(id));
    }
}
