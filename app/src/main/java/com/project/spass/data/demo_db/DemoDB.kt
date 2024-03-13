package com.project.spass.data.demo_db

import com.project.spass.domain.model.PassModel
import javax.inject.Inject

class DemoDB @Inject constructor() {
    fun getPasses(): List<PassModel> {
        return listOf(
            PassModel(
                id = 1,
                source = "Uran",
                destination = "Mohapada",
                price = 1200.00
            ),

            //second product
            PassModel(
                id = 2,
                source = "Pen",
                destination = "Mohapada",
                price = 1000.00
            ),

            //third product
            PassModel(
                id = 3,
                source = "Karjat",
                destination = "Mohapada",
                price = 800.00
            ),
        )
    }
}