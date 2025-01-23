package io.github.mamedovilkin.finexetf.view.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.rubengees.introduction.IntroductionBuilder
import com.rubengees.introduction.Slide
import com.rubengees.introduction.style.FullscreenStyle
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.finexetf.BuildConfig
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val sharedPreferences by lazy {
        getSharedPreferences(
            "${BuildConfig.APPLICATION_ID}_sharedPreferences",
            Context.MODE_PRIVATE
        )
    }

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null")

    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (!sharedPreferences.getBoolean("introduction", false)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                IntroductionBuilder(this)
                    .withSlides(getSlides())
                    .withForcedOrientation(IntroductionBuilder.ORIENTATION_PORTRAIT)
                    .withStyle(FullscreenStyle())
                    .withTypeface(resources.getFont(R.font.nunito))
                    .introduceMyself()
            } else {
                IntroductionBuilder(this)
                    .withSlides(getSlides())
                    .withForcedOrientation(IntroductionBuilder.ORIENTATION_PORTRAIT)
                    .withStyle(FullscreenStyle())
                    .introduceMyself()
            }

            val edit = sharedPreferences.edit()
            edit.putBoolean("introduction", true)
            edit.apply()
        }

        binding.apply {
            setSupportActionBar(materialToolbar)

            navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
            val navController = navHostFragment.navController
            val appBarConfiguration = AppBarConfiguration(
                setOf(R.id.my_assets, R.id.history, R.id.blog)
            )

            materialToolbar.setupWithNavController(navController, appBarConfiguration)
            setupActionBarWithNavController(navController, appBarConfiguration)
            bottomNavigationView.setupWithNavController(navController)

            bottomNavigationView.setOnItemSelectedListener { item ->
                if (item.itemId != bottomNavigationView.selectedItemId) {
                    navController.popBackStack(item.itemId, inclusive = true, saveState = false)
                    navController.navigate(item.itemId)
                }
                true
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navHostFragment.navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }

        return true
    }

    private fun getSlides(): List<Slide> {
        val slides = mutableListOf<Slide>()

        slides.add(
            Slide()
                .withTitle(resources.getString(R.string.welcome))
                .withDescription(resources.getString(R.string.welcome_summary))
                .withImage(R.drawable.introduction_logo)
                .withColorResource(R.color.colorPrimarySecondary)
        )

        slides.add(
            Slide()
                .withTitle(resources.getString(R.string.my_assets))
                .withDescription(resources.getString(R.string.my_assets_summary))
                .withImage(R.drawable.slide_my_assets)
                .withColorResource(R.color.colorPrimarySecondary)
        )

        slides.add(
            Slide()
                .withTitle(resources.getString(R.string.fund))
                .withDescription(resources.getString(R.string.fund_summary))
                .withImage(R.drawable.slide_fund)
                .withColorResource(R.color.colorPrimarySecondary)
        )

        slides.add(
            Slide()
                .withTitle(resources.getString(R.string.add_purchase_button))
                .withDescription(resources.getString(R.string.add_purchase_button_summary))
                .withImage(R.drawable.slide_my_assets)
                .withColorResource(R.color.colorPrimarySecondary)
        )

        slides.add(
            Slide()
                .withTitle(resources.getString(R.string.add_sell_button))
                .withDescription(resources.getString(R.string.add_sell_button_summary))
                .withImage(R.drawable.slide_my_assets)
                .withColorResource(R.color.colorPrimarySecondary)
        )

        slides.add(
            Slide()
                .withTitle(resources.getString(R.string.choose_fund))
                .withDescription(resources.getString(R.string.choose_fund_summary))
                .withImage(R.drawable.slide_choose_fund)
                .withColorResource(R.color.colorPrimarySecondary)
        )

        slides.add(
            Slide()
                .withTitle(resources.getString(R.string.add_purchase))
                .withDescription(resources.getString(R.string.add_purchase_summary))
                .withImage(R.drawable.slide_add_purchase)
                .withColorResource(R.color.colorPrimarySecondary)
        )

        slides.add(
            Slide()
                .withTitle(resources.getString(R.string.history))
                .withDescription(resources.getString(R.string.history_summary))
                .withImage(R.drawable.slide_history)
                .withColorResource(R.color.colorPrimarySecondary)
        )

        slides.add(
            Slide()
                .withTitle(resources.getString(R.string.blog))
                .withDescription(resources.getString(R.string.blog_summary))
                .withImage(R.drawable.slide_blog)
                .withColorResource(R.color.colorPrimarySecondary)
        )

        return slides
    }
}