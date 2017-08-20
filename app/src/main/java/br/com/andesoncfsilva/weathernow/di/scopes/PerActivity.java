package br.com.andesoncfsilva.weathernow.di.scopes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Anderson Silva on 16/08/17.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {
}
