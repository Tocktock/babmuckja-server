package com.toyproject.babmuckja.supplier

import com.toyproject.babmuckja.supplier.dtos.CreateSupplierDto
import com.toyproject.babmuckja.supplier.dtos.GetSupplierDetailResultDto
import com.toyproject.babmuckja.supplier.entities.SupplierEntity
import com.toyproject.babmuckja.supplier.repositories.QSupplierRepository
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/supplier")
class SupplierController(
    val supplierService: SupplierService,
    val qSupplierRepository: QSupplierRepository
) {

    @GetMapping("/paging/{category}")
    @Throws(Exception::class)
    fun getPageWithCategory(
        @PathVariable(name = "category") category: String,
        @RequestParam(name = "page", defaultValue = "0") page: String
    ): Page<SupplierEntity?>? {
        return supplierService.getPageWithCategory(category, page)
    }

    @GetMapping("/detail/{id}")
    fun getSupllier(@PathVariable(name = "id") supplierId: Long): SupplierEntity {
        return supplierService.getSupplierById(supplierId)
    }

    @GetMapping("/all")
    @Throws(Exception::class)
    fun get10SupplierName(): List<String> {
        return qSupplierRepository.getAllSupplierName()
    }

    @PostMapping("/add")
    @Throws(Exception::class)
    fun createSupplier(@RequestBody @Valid createSupplierDto: CreateSupplierDto) {
        supplierService.createSupplier(createSupplierDto)
    }

    @PostMapping("/seed")
    @Throws(Exception::class)
    fun seedSupplier(@RequestBody @Valid createSupplierDto: List<CreateSupplierDto>)
            : Boolean {
        supplierService.seedData(createSupplierDto)
        return true
    }
}