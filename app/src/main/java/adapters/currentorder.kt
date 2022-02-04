package adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madproject.R
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import datamodels.CurrentOrderItem

class currentorder (var context: Context,
    private var currentOrderList: ArrayList<CurrentOrderItem>,
    //private val listener: currentorder.OnItemClickListener
                    private val listener:OnItemClickListener
):
    RecyclerView.Adapter<currentorder.ItemListViewHolder>() {

    interface OnItemClickListener {
        fun showQRCode(orderID: String)
        fun cancelOrder(position: Int)
    }

    class ItemListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val takeAwayTimeTV: TextView = itemView.findViewById(R.id.current_order_item_take_away_time_tv)
        val paymentStatusTV: TextView = itemView.findViewById(R.id.current_order_item_payment_status_tv)
        val orderIDTV: TextView = itemView.findViewById(R.id.current_order_item_order_id_tv)

        val totalItemPriceTV: TextView = itemView.findViewById(R.id.current_order_item_total_price_tv)
        val totalTaxTV: TextView = itemView.findViewById(R.id.current_order_item_total_tax_tv)
        val subTotalTV: TextView = itemView.findViewById(R.id.current_order_item_sub_total_tv)
        val showQRBtn: ExtendedFloatingActionButton = itemView.findViewById(R.id.current_order_item_show_qr_btn)
        val cancelBtn: ExtendedFloatingActionButton = itemView.findViewById(R.id.current_order_item_cancel_btn)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): currentorder.ItemListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.current_order_item, parent, false)
        return currentorder.ItemListViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: currentorder.ItemListViewHolder, position: Int) {

        val currentItem = currentOrderList[position]

        holder.takeAwayTimeTV.text = currentItem.takeAwayTime
        holder.paymentStatusTV.text = currentItem.paymentStatus
        holder.orderIDTV.text = currentItem.orderID
        holder.totalItemPriceTV.text = "\$%.2f".format(currentItem.totalItemPrice.toFloat())
        holder.totalTaxTV.text = "\$%.2f".format(currentItem.tax.toFloat())
        holder.subTotalTV.text = "\$%.2f".format(currentItem.subTotal.toFloat())

        holder.showQRBtn.setOnClickListener {
            listener.showQRCode(currentItem.orderID)
        }
        holder.cancelBtn.setOnClickListener {
            listener.cancelOrder(position)
        }

    }
    override fun getItemCount(): Int = currentOrderList.size

    }