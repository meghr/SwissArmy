package com.attri.swissarmy.feature.pdftools.domain;

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
public final class PdfToolsRepository_Factory implements Factory<PdfToolsRepository> {
  private final Provider<Context> contextProvider;

  public PdfToolsRepository_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public PdfToolsRepository get() {
    return newInstance(contextProvider.get());
  }

  public static PdfToolsRepository_Factory create(Provider<Context> contextProvider) {
    return new PdfToolsRepository_Factory(contextProvider);
  }

  public static PdfToolsRepository newInstance(Context context) {
    return new PdfToolsRepository(context);
  }
}
