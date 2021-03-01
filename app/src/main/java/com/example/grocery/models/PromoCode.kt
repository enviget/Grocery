package com.example.grocery.models

class PromoCode {

    companion object {
        val KEY_PROMO1: String = "promo1"
        val KEY_PROMO2: String = "promo2"
        val KEY_PROMO3: String = "promo3"

        val PROMO_PERCENT1: Double = .75
        val PROMO_PERCENT2: Double = .65
        val PROMO_PERCENT3: Double = .50


        fun addPromo(inputCode: String): Double {
            var promoList = getPromoList()
            var percent: Double = 1.0
            for (i in promoList)
                if (promoList.containsKey(inputCode))
                    percent = promoList.getValue(inputCode)

            return percent
        }

        fun getPromoList(): HashMap<String, Double> {
            var promoList: HashMap<String, Double> = HashMap()
            promoList.put(KEY_PROMO1, PROMO_PERCENT1)
            promoList.put(KEY_PROMO2, PROMO_PERCENT2)
            promoList.put(KEY_PROMO3, PROMO_PERCENT3)

            return promoList
        }


    }
}