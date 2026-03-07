package com.showedup.app.data.repository;

import com.showedup.app.crypto.CanonicalJson;
import com.showedup.app.crypto.HashUtils;
import com.showedup.app.data.dao.SecurityEventDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class SecurityRepository_Factory implements Factory<SecurityRepository> {
  private final Provider<SecurityEventDao> securityEventDaoProvider;

  private final Provider<HashUtils> hashUtilsProvider;

  private final Provider<CanonicalJson> canonicalJsonProvider;

  public SecurityRepository_Factory(Provider<SecurityEventDao> securityEventDaoProvider,
      Provider<HashUtils> hashUtilsProvider, Provider<CanonicalJson> canonicalJsonProvider) {
    this.securityEventDaoProvider = securityEventDaoProvider;
    this.hashUtilsProvider = hashUtilsProvider;
    this.canonicalJsonProvider = canonicalJsonProvider;
  }

  @Override
  public SecurityRepository get() {
    return newInstance(securityEventDaoProvider.get(), hashUtilsProvider.get(), canonicalJsonProvider.get());
  }

  public static SecurityRepository_Factory create(
      Provider<SecurityEventDao> securityEventDaoProvider, Provider<HashUtils> hashUtilsProvider,
      Provider<CanonicalJson> canonicalJsonProvider) {
    return new SecurityRepository_Factory(securityEventDaoProvider, hashUtilsProvider, canonicalJsonProvider);
  }

  public static SecurityRepository newInstance(SecurityEventDao securityEventDao,
      HashUtils hashUtils, CanonicalJson canonicalJson) {
    return new SecurityRepository(securityEventDao, hashUtils, canonicalJson);
  }
}
