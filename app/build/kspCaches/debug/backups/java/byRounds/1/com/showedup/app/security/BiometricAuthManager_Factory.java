package com.showedup.app.security;

import android.content.Context;
import com.showedup.app.data.repository.SecurityRepository;
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
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class BiometricAuthManager_Factory implements Factory<BiometricAuthManager> {
  private final Provider<Context> contextProvider;

  private final Provider<SecurityRepository> securityRepositoryProvider;

  public BiometricAuthManager_Factory(Provider<Context> contextProvider,
      Provider<SecurityRepository> securityRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.securityRepositoryProvider = securityRepositoryProvider;
  }

  @Override
  public BiometricAuthManager get() {
    return newInstance(contextProvider.get(), securityRepositoryProvider.get());
  }

  public static BiometricAuthManager_Factory create(Provider<Context> contextProvider,
      Provider<SecurityRepository> securityRepositoryProvider) {
    return new BiometricAuthManager_Factory(contextProvider, securityRepositoryProvider);
  }

  public static BiometricAuthManager newInstance(Context context,
      SecurityRepository securityRepository) {
    return new BiometricAuthManager(context, securityRepository);
  }
}
