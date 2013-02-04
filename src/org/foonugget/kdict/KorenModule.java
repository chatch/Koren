
package org.foonugget.kdict;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Named;

public class KorenModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().build(WordSearchListAdapter.Factory.class));
    }

    @Provides
    @Named("sharedPreferences")
    SharedPreferences sharedPrefs(Context ctx) {
        return ctx.getSharedPreferences(KorenModule.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

}
