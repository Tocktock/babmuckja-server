package com.toyproject.babmuckja.supplier

import com.toyproject.babmuckja.product.ProductService
import com.toyproject.babmuckja.product.constants.SeedProduct
import com.toyproject.babmuckja.product.entities.ProductEntity
import com.toyproject.babmuckja.supplier.constants.SupplierCategory
import com.toyproject.babmuckja.supplier.dtos.CreateSupplierDto
import com.toyproject.babmuckja.supplier.dtos.GetSupplierDetailResultDto
import com.toyproject.babmuckja.supplier.entities.SupplierEntity
import com.toyproject.babmuckja.supplier.repositories.QSupplierRepository
import com.toyproject.babmuckja.supplier.repositories.SupplierRepository
import javassist.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class SupplierService(
    val supplierRepository: SupplierRepository,
    val qSupplierRepository: QSupplierRepository,
    val productService: ProductService
) {

    fun getPageWithCategory(category: String, page: String): Page<SupplierEntity?>? {

        val paging = PageRequest.of(page.toInt(), 6);
        return supplierRepository.findByCategory(SupplierCategory.valueOf(category), paging)
    }

    fun createSupplier(createSupplierDto: CreateSupplierDto) {
        val supplier = SupplierEntity(
            createSupplierDto.supplierName,
            SupplierCategory.valueOf(createSupplierDto.category),
            createSupplierDto.contactInfo,
            createSupplierDto.location,
            createSupplierDto.latitude,
            createSupplierDto.longitude
        )
        supplierRepository.save(supplier)
    }

    fun getSupplierById(supplierId: Long): SupplierEntity {
        val supplier = supplierRepository.findById(supplierId).get()
        return supplier
    }

    fun seedData(createSupplierDtoList: List<CreateSupplierDto>) {

        val seedProduct = SeedProduct()
        var supplierList = mutableListOf<SupplierEntity>()
        var productList = mutableSetOf<ProductEntity>()
        createSupplierDtoList.forEach {
            var supplier = SupplierEntity(
                it.supplierName,
                SupplierCategory.valueOf(it.category),
                it.contactInfo,
                it.location,
                it.latitude,
                it.longitude
            )
            var product1 =
                ProductEntity(
                    seedProduct.getRandomMenu(),
                    seedProduct.getRandomPrice(),
                    supplier
                )
            var product2 =
                ProductEntity(
                    seedProduct.getRandomMenu(),
                    seedProduct.getRandomPrice(),
                    supplier
                )
            var product3 =
                ProductEntity(
                    seedProduct.getRandomMenu(),
                    seedProduct.getRandomPrice(),
                    supplier
                )
            supplier.products.add(product1)
            supplier.products.add(product2)
            supplier.products.add(product3)
            supplierList.add(supplier)
            productList.add(product1)
            productList.add(product2)
            productList.add(product3)
        }
        supplierRepository.saveAll(supplierList)
        productService.seedProduct(productList)
    }
}