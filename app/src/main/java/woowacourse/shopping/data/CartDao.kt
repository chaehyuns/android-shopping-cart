package woowacourse.shopping.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import woowacourse.shopping.data.model.CartItemEntity

@Dao
interface CartDao {
    @Insert
    fun save(cartItemEntity: CartItemEntity)

    @Query("UPDATE cart_items SET quantity = :quantity WHERE productId = :productId")
    fun updateQuantity(
        productId: Long,
        quantity: Int,
    )

    @Query("SELECT COUNT(*) FROM cart_items")
    fun size(): Int

    @Query("SELECT * FROM cart_items WHERE productId = :productId")
    fun findWithProductId(productId: Long): CartItemEntity

    @Query("SELECT * FROM cart_items WHERE id = :id")
    fun find(id: Long): CartItemEntity

    @Query("SELECT * FROM cart_items")
    fun findAll(): List<CartItemEntity>

    @Query("SELECT SUM(quantity) FROM cart_items")
    fun sumQuantity(): Int

    @Query("SELECT * FROM cart_items LIMIT :limit OFFSET :offset")
    fun findByPaged(
        offset: Int,
        limit: Int,
    ): List<CartItemEntity>

    @Query("DELETE FROM cart_items WHERE id = :id")
    fun delete(id: Long)

    @Query("DELETE FROM cart_items")
    fun deleteAll()

    @Query("SELECT quantity FROM cart_items WHERE productId = :productId")
    fun getQuantityByProductId(productId: Long): Int?
}
