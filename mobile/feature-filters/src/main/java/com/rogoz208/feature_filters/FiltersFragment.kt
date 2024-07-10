package com.rogoz208.feature_filters

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.util.Pair
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.rogoz208.feature_filters.databinding.FragmentFiltersBinding
import com.rogoz208.feature_filters.di.DaggerFeatureFiltersComponent
import com.rogoz208.feature_filters.di.FeatureFiltersComponent
import com.rogoz208.mobile_common.model.Filter
import com.rogoz208.mobile_common.di.ViewModelFactory
import com.rogoz208.newsdata_api.model.Language
import com.rogoz208.newsdata_api.model.SortBy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class FiltersFragment : Fragment(R.layout.fragment_filters), MenuProvider,
    IHasComponent<FeatureFiltersComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<FiltersViewModel> { viewModelFactory }

    private val binding by viewBinding(FragmentFiltersBinding::bind)

    private var materialDateRangePicker: MaterialDatePicker<Pair<Long, Long>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var resultKey: String? = null
        var filter: Filter? = null
        arguments?.let {
            resultKey = it.getString(RESULT_KEY_EXTRA_KEY)!!
            filter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(FILTER_EXTRA_KEY, Filter::class.java)!!
            } else {
                it.getParcelable(FILTER_EXTRA_KEY)!!
            }
        }
        viewModel.obtainEvent(UIEvent.LoadData(resultKey!!, filter!!))

        initToolbar()
        initSortByToggleButton()
        setDatePickerSegmentColorsActivated(false)
        binding.datePickerIconButton.setOnClickListener {
            showDateRangePicker()
        }
        initViewModel()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.filters_toolbar_menu, menu)
    }

    override fun getComponent(): FeatureFiltersComponent {
        return DaggerFeatureFiltersComponent.builder()
            .featureFiltersDependencies(XInjectionManager.findComponent())
            .build()
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.toolbar_menu_item_submit -> {
                val sortBy = when (binding.sortByToggleButton.checkedButtonId) {
                    R.id.sort_by_popular_button -> SortBy.POPULARITY
                    R.id.sort_by_relevant_button -> SortBy.RELEVANCY
                    R.id.sort_by_new_button -> SortBy.PUBLISHED_AT
                    else -> null
                }

                val language = when (binding.languagesChipGroup.checkedChipId) {
                    R.id.english_chip -> Language.EN
                    R.id.deutsch_chip -> Language.DE
                    R.id.russian_chip -> Language.RU
                    else -> null
                }

                val dateFrom = materialDateRangePicker?.selection?.first?.let { Date(it) }
                val dateTo = materialDateRangePicker?.selection?.second?.let { Date(it) }

                viewModel.obtainEvent(UIEvent.SaveData(Filter(sortBy, language, dateFrom, dateTo)))
                return true
            }
        }
        return false
    }

    private fun initToolbar() {
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            binding.toolbar.setNavigationOnClickListener { viewModel.obtainEvent(UIEvent.BackPressed) }
            addMenuProvider(this@FiltersFragment, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }
    }

    private fun initSortByToggleButton() {
        binding.sortByToggleButton.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            val icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_check_24dp)

            when (checkedId) {
                R.id.sort_by_relevant_button -> {
                    if (isChecked) {
                        toggleButton.findViewById<MaterialButton>(checkedId).icon = icon
                    } else {
                        toggleButton.findViewById<MaterialButton>(checkedId).icon = null
                    }
                }

                R.id.sort_by_new_button -> {
                    if (isChecked) {
                        toggleButton.findViewById<MaterialButton>(checkedId).icon = icon
                    } else {
                        toggleButton.findViewById<MaterialButton>(checkedId).icon = null
                    }
                }

                R.id.sort_by_popular_button -> {
                    if (isChecked) {
                        toggleButton.findViewById<MaterialButton>(checkedId).icon = icon
                    } else {
                        toggleButton.findViewById<MaterialButton>(checkedId).icon = null
                    }
                }
            }
        }
    }

    private fun showDateRangePicker() {
        materialDateRangePicker = MaterialDatePicker.Builder
            .dateRangePicker()
            .setTheme(R.style.ThemeMaterialCalendar)
            .setSelection(Pair(null, null))
            .build()

        materialDateRangePicker?.addOnPositiveButtonClickListener { selection ->
            val range = formatDateRange(Date(selection.first), Date(selection.second))
            binding.dateRangeTextView.text = range
            setDatePickerSegmentColorsActivated(true)
        }

        materialDateRangePicker?.addOnNegativeButtonClickListener {
            materialDateRangePicker?.dismiss()
        }

        materialDateRangePicker?.showsDialog = true
        materialDateRangePicker?.show(requireActivity().supportFragmentManager, DATE_PICKER_TAG)
    }

    private fun setDatePickerSegmentColorsActivated(isDatePickerActivated: Boolean) {
        val theme = requireActivity().theme
        val iconDefaultColor =
            resources.getColor(com.rogoz208.mobile_common.R.color.light_neutral_variant_30, theme)
        val iconActiveColor = resources.getColor(com.rogoz208.mobile_common.R.color.white, theme)

        val textDefaultColor =
            resources.getColor(com.rogoz208.mobile_common.R.color.light_neutral_variant_50, theme)
        val textActiveColor =
            resources.getColor(com.rogoz208.mobile_common.R.color.light_primary_40, theme)

        val backgroundDefaultColor = Color.TRANSPARENT
        val backgroundActiveColor =
            resources.getColor(com.rogoz208.mobile_common.R.color.light_primary_40, theme)

        val states = arrayOf(intArrayOf(android.R.attr.state_enabled))
        val myList: ColorStateList

        if (isDatePickerActivated) {
            val colors = intArrayOf(iconActiveColor)
            myList = ColorStateList(states, colors)

            with(binding) {
                datePickerIconButton.setBackgroundColor(backgroundActiveColor)
                dateRangeTextView.setTextColor(textActiveColor)
            }
        } else {
            val colors = intArrayOf(iconDefaultColor)
            myList = ColorStateList(states, colors)

            with(binding) {
                datePickerIconButton.setBackgroundColor(backgroundDefaultColor)
                dateRangeTextView.setTextColor(textDefaultColor)
            }
        }
        binding.datePickerIconButton.iconTint = myList
    }

    private fun initViewModel() {
        viewModel.newUIState.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: UIState) {
        with(binding) {
            val sortBy = state.filter.sortBy
            val language = state.filter.language
            val from = state.filter.from
            val to = state.filter.to

            when (sortBy) {
                SortBy.RELEVANCY -> sortByToggleButton.check(R.id.sort_by_relevant_button)
                SortBy.POPULARITY -> sortByToggleButton.check(R.id.sort_by_popular_button)
                SortBy.PUBLISHED_AT -> sortByToggleButton.check(R.id.sort_by_new_button)
                null -> {}
            }

            when (language) {
                Language.DE -> languagesChipGroup.check(R.id.deutsch_chip)
                Language.EN -> languagesChipGroup.check(R.id.english_chip)
                Language.RU -> languagesChipGroup.check(R.id.russian_chip)
                null -> {}
            }

            if (from != null && to != null) {
                val formattedDateRange = formatDateRange(from, to)

                dateRangeTextView.text = formattedDateRange
                setDatePickerSegmentColorsActivated(true)
            }

        }
    }

    private fun formatDateRange(dateFrom: Date?, dateTo: Date?): String {
        val dateFromString = dateFrom?.let { SimpleDateFormat("MMM dd", Locale.US).format(it) }
        val dateToString = dateTo?.let { SimpleDateFormat("MMM dd, yyyy", Locale.US).format(it) }

        return "$dateFromString-$dateToString"
    }

    companion object {
        const val RESULT_KEY_EXTRA_KEY = "RESULT_KEY_EXTRA_KEY"
        const val FILTER_EXTRA_KEY = "FILTER_EXTRA_KEY"

        const val DATE_PICKER_TAG = "DATE_PICKER_TAG"

        fun newInstance(resultKey: String, filter: Filter): FiltersFragment =
            FiltersFragment().apply {
                arguments = bundleOf(RESULT_KEY_EXTRA_KEY to resultKey, FILTER_EXTRA_KEY to filter)
            }
    }
}

fun createFiltersScreen(resultKey: String, filter: Filter): Fragment =
    FiltersFragment.newInstance(resultKey, filter)