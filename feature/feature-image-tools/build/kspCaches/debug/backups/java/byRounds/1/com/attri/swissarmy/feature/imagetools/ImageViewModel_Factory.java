package com.attri.swissarmy.feature.imagetools;

import com.attri.swissarmy.feature.imagetools.domain.ImageToolsRepository;
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
public final class ImageViewModel_Factory implements Factory<ImageViewModel> {
  private final Provider<ImageToolsRepository> repositoryProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public ImageViewModel_Factory(Provider<ImageToolsRepository> repositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.repositoryProvider = repositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public ImageViewModel get() {
    return newInstance(repositoryProvider.get(), settingsRepositoryProvider.get());
  }

  public static ImageViewModel_Factory create(Provider<ImageToolsRepository> repositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new ImageViewModel_Factory(repositoryProvider, settingsRepositoryProvider);
  }

  public static ImageViewModel newInstance(ImageToolsRepository repository,
      SettingsRepository settingsRepository) {
    return new ImageViewModel(repository, settingsRepository);
  }
}
