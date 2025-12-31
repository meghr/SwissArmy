package com.attri.swissarmy.feature.calculators.domain;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
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
public final class TaxCalculatorRepository_Factory implements Factory<TaxCalculatorRepository> {
  @Override
  public TaxCalculatorRepository get() {
    return newInstance();
  }

  public static TaxCalculatorRepository_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static TaxCalculatorRepository newInstance() {
    return new TaxCalculatorRepository();
  }

  private static final class InstanceHolder {
    private static final TaxCalculatorRepository_Factory INSTANCE = new TaxCalculatorRepository_Factory();
  }
}
