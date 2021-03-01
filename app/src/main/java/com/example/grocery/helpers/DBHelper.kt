package com.example.grocery.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.grocery.models.Product

class DBHelper(var mContext: Context) : SQLiteOpenHelper(mContext, FILE_NAME, null, KEY_VERSION) {

    private var database: SQLiteDatabase = this.writableDatabase

    companion object {
        const val KEY_VERSION = 7
        const val FILE_NAME = "db_file1"
        const val TABLE_NAME = "grocery_cart"
        const val COLUMN_NAME = "name"
        const val COLUMN_PRICE = "price"
        const val COLUMN_MRP = "mrp"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_PRODUCT_ID = "product_id"
        const val COLUMN_QUANTITY = "quantity"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        var createTable = "create table $TABLE_NAME(" +
                "$COLUMN_NAME char(50), " +
                "$COLUMN_PRICE double, " +
                "$COLUMN_MRP double, " +
                "$COLUMN_IMAGE char(250)," +
                "$COLUMN_PRODUCT_ID char(200) PRIMARY KEY," +
                "$COLUMN_QUANTITY INTEGER )"
        db?.execSQL(createTable)
        Log.d("abc", "table created")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val dropTable = "DROP TABLE $TABLE_NAME"
        db?.execSQL(dropTable)
        onCreate(db)
    }

    fun isItemInCart(product: Product): Boolean {
        var query = "Select * from $TABLE_NAME where $COLUMN_PRODUCT_ID = ?"
        var cursor = database.rawQuery(query, arrayOf(product._id))
        var count = cursor.count
        return count != 0
        Log.d("abc", "${return count != 0}")
    }

    fun addItem(product: Product) {
        if (!isItemInCart(product)) {
            var contentValues = ContentValues()
            contentValues.put(COLUMN_NAME, product.productName)
            contentValues.put(COLUMN_PRICE, product.price)
            contentValues.put(COLUMN_MRP, product.mrp)
            contentValues.put(COLUMN_QUANTITY, 1)
            contentValues.put(COLUMN_IMAGE, product.image)
            contentValues.put(COLUMN_PRODUCT_ID, product._id)

            database.insert(TABLE_NAME, null, contentValues)
            Log.d("abc", "Item Inserted")
        } else {
            Log.d("abc", "Item Not Inserted")
        }
    }

    fun deleteItem(product: Product) {
        var whereClause = "$COLUMN_PRODUCT_ID = ?"
        var whereArgs = arrayOf(product._id)
        database.delete(TABLE_NAME, whereClause, whereArgs)
        Log.d("abc", "Item Deleted")
    }

    fun getItemQuantity(product: Product): Int {
        var quantity = 0
        var query = "select * from $TABLE_NAME where $COLUMN_PRODUCT_ID = ?"
        var cursor = database.rawQuery(query, arrayOf(product._id))
        if (cursor.moveToFirst()) {
            quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY))
            cursor.close()
            return quantity
        }
        return quantity
    }

    fun updateQuantity(product: Product) {
        if (product.quantity != 0) {
            var contentValues = ContentValues()
            contentValues.put(COLUMN_QUANTITY, product.quantity)
            var whereClause = "$COLUMN_PRODUCT_ID = ?"
            var whereArgs = arrayOf(product._id)
            database.update(TABLE_NAME, contentValues, whereClause, whereArgs)
            Log.d("abc", "Item Quantity updated")
        }
    }

    fun readData(): ArrayList<Product> {
        var List = ArrayList<Product>()
        var columns = arrayOf(
            COLUMN_PRODUCT_ID,
            COLUMN_NAME,
            COLUMN_PRICE,
            COLUMN_MRP,
            COLUMN_QUANTITY,
            COLUMN_IMAGE
        )
        var cursor = database.query(TABLE_NAME, columns, null, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                var product = Product()
                product._id = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_ID))
                product.productName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                product.price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE))
                product.mrp = cursor.getDouble(cursor.getColumnIndex(COLUMN_MRP))
                product.quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY))
                product.image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE))
                List.add(product)
            } while (cursor.moveToNext())
        }
        return List
    }

    fun getTotal(): Double {
        var columns = arrayOf(
            COLUMN_PRODUCT_ID,
            COLUMN_NAME,
            COLUMN_PRICE,
            COLUMN_MRP,
            COLUMN_QUANTITY,
            COLUMN_IMAGE
        )
        var total = 0.0
        var cursor = database.query(TABLE_NAME, columns, null, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                var price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE))
                var quantity = cursor.getDouble(cursor.getColumnIndex(COLUMN_QUANTITY))
                total += price*quantity
            } while (cursor.moveToNext())

        }
        return total
    }

    fun getMRP(): Double {
        var columns = arrayOf(
            COLUMN_PRODUCT_ID,
            COLUMN_NAME,
            COLUMN_PRICE,
            COLUMN_MRP,
            COLUMN_QUANTITY,
            COLUMN_IMAGE
        )
        var total = 0.0
        var cursor = database.query(TABLE_NAME, columns, null, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                var mrp = cursor.getDouble(cursor.getColumnIndex(COLUMN_MRP))
                var quantity = cursor.getDouble(cursor.getColumnIndex(COLUMN_QUANTITY))
                total += mrp*quantity
            } while (cursor.moveToNext())

        }
        return total
    }

    fun getTotalQuantity():Int{
        var total = 0
        var columns = arrayOf(COLUMN_QUANTITY)
        var cursor = database.query(TABLE_NAME, columns,null,null,null,null,null)
        if(cursor!= null && cursor.moveToFirst()){
            do{
                total += cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY))
            }
                while(cursor.moveToNext())
        }
        return total
    }

    fun clearTable(){
        database.delete(TABLE_NAME,null,null)
    }
}