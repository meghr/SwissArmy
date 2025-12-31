package com.attri.swissarmy.feature.imagetools.domain;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class ImageToolsRepository_Factory implements Factory<ImageToolsRepository> {
  private final Provider<Context> contextProvider;

  public ImageToolsRepository_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ImageToolsRepository get() {
    return newInstance(contextProvider.get());
  }

  public static ImageToolsRepository_Factory create(Provider<Context> contextProvider) {
    return new ImageToolsRepository_Factory(contextProvider);
  }

  public static ImageToolsRepository newInstance(Context context) {
    return new ImageToolsRepository(context);
  }
}
