package com.attri.swissarmy.feature.pdftools;

import com.attri.swissarmy.feature.pdftools.domain.PdfToolsRepository;
import com.attri.swissarmy.feature.settings.domain.SettingsRepository;
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
public final class PdfViewModel_Factory implements Factory<PdfViewModel> {
  private final Provider<PdfToolsRepository> repositoryProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public PdfViewModel_Factory(Provider<PdfToolsRepository> repositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.repositoryProvider = repositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public PdfViewModel get() {
    return newInstance(repositoryProvider.get(), settingsRepositoryProvider.get());
  }

  public static PdfViewModel_Factory create(Provider<PdfToolsRepository> repositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new PdfViewModel_Factory(repositoryProvider, settingsRepositoryProvider);
  }

  public static PdfViewModel newInstance(PdfToolsRepository repository,
      SettingsRepository settingsRepository) {
    return new PdfViewModel(repository, settingsRepository);
  }
}
