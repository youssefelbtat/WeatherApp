import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.model.getDamyList
import com.example.weatherapp.ui.home.view.DailyWeatherAdabter
import com.example.weatherapp.ui.home.view.HourlyWeatherAdabter

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var hourlyRecyclerAdapter: HourlyWeatherAdabter
    private lateinit var dailyRecyclerAdapter: DailyWeatherAdabter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setUpRecyclerViews()

    }

    private fun init() {
        hourlyRecyclerAdapter = HourlyWeatherAdabter()
        dailyRecyclerAdapter = DailyWeatherAdabter()
    }

    private fun setUpRecyclerViews() {
        binding.rvHourlyWeather.apply {
            adapter = hourlyRecyclerAdapter.apply {
                submitList(getDamyList.mutableList)
            }
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
        binding.rvDailyWeather.apply {
            adapter = dailyRecyclerAdapter.apply {
                submitList(getDamyList.dailyWeatherList)
            }
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context)
        }
    }
}
