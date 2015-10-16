package models

case class ProductEntity(id: Long,
                         authorId: Long,
                         category: String,
                         title: String,
                         productUrl: String,
                         clickPrice: Double,
                         campaignBudget: Double,
                         viewsCount: Long,
                         clicksCount: Long,
                         productImage: String,
                         productPrice: Double,
                         isCrossSellEnabled: Boolean,
                         isBannerSellingEnabled: Boolean,
                         country: String)
