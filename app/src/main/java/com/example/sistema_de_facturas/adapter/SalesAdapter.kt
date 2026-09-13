package com.example.sistema_de_facturas.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistema_de_facturas.R
import com.example.sistema_de_facturas.model.Sale

class SalesAdapter(private var sales: List<Sale>) : RecyclerView.Adapter<SalesAdapter.SaleViewHolder>() {

    class SaleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvProductType: TextView = view.findViewById(R.id.tvProductType)
        val tvProductName: TextView = view.findViewById(R.id.tvProductName)
        val tvSaleCode: TextView = view.findViewById(R.id.tvSaleCode)
        val tvPriceQty: TextView = view.findViewById(R.id.tvPriceQty)
        val tvTotal: TextView = view.findViewById(R.id.tvTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sale, parent, false)
        return SaleViewHolder(view)
    }

    override fun onBindViewHolder(holder: SaleViewHolder, position: Int) {
        val sale = sales[position]
        holder.tvProductType.text = sale.type
        holder.tvProductName.text = sale.name
        holder.tvSaleCode.text = "Code: ${sale.code}"
        holder.tvPriceQty.text = "${sale.price} x ${sale.quantity}"
        holder.tvTotal.text = "Total: $${sale.total}"

        // Each document type keeps its own color representation
        if (sale.type == "FACTURA") {
            holder.tvProductType.setTextColor(Color.BLUE)
        } else {
            holder.tvProductType.setTextColor(Color.GREEN)
        }
    }

    override fun getItemCount(): Int = sales.size

    fun updateData(newSales: List<Sale>) {
        this.sales = newSales
        notifyDataSetChanged()
    }
}
