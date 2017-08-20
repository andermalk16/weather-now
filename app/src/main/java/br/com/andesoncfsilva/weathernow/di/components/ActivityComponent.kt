package br.com.andesoncfsilva.weathernow.di.components


import android.app.Activity
import br.com.andesoncfsilva.weathernow.di.modules.ActivityModule
import br.com.andesoncfsilva.weathernow.di.scopes.PerActivity
import br.com.andesoncfsilva.weathernow.views.MainActivity
import dagger.Component

/**
 * A base component upon which fragment's components may depend.
 * Activity-level components should extend this component.

 * Subtypes of ActivityComponent should be decorated with annotation:
 */
@PerActivity
@Component(
        dependencies = arrayOf(AppComponent::class),
        modules = arrayOf(ActivityModule::class)
)
interface ActivityComponent {

    fun activity(): Activity
    fun inject(mainActivity: MainActivity)
}
