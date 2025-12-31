package com.attri.swissarmy.feature.calculators;

import com.attri.swissarmy.feature.calculators.domain.TaxCalculatorRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class CalculatorViewModel_Factory implements Factory<CalculatorViewModel> {
  private final Provider<TaxCalculatorRepository> repositoryProvider;

  public CalculatorViewModel_Factory(Provider<TaxCalculatorRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public CalculatorViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static CalculatorViewModel_Factory create(
      Provider<TaxCalculatorRepository> repositoryProvider) {
    return new CalculatorViewModel_Factory(repositoryProvider);
  }

  public static CalculatorViewModel newInstance(TaxCalculatorRepository repository) {
    return new CalculatorViewModel(repository);
  }
}
