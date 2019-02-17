package bldg5.jj.pgnhelper.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import bldg5.jj.pgnhelper.Game
import bldg5.jj.pgnhelper.R
import kotlinx.android.synthetic.main.listed_game.view.*


class GamesAdapter(context: Context,
                   private val dataSource: List<Game>): BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.listed_game, parent, false)

        dataSource[position].apply {
            rowView._id.text = id.toString()
            rowView.white_plus_elo.text = "White: " + White + (if (WhiteELO.isNullOrEmpty()) "" else " ($WhiteELO)")
            rowView.black_plus_elo.text = "Black: " + Black + (if (BlackELO.isNullOrEmpty()) "" else " ($BlackELO)")

            rowView.event_site_date.text = description()
        }

        return rowView
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dataSource.size
    }
}