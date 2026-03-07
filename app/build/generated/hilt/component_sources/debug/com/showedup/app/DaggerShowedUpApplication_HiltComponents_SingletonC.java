package com.showedup.app;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.showedup.app.crypto.CanonicalJson;
import com.showedup.app.crypto.HashUtils;
import com.showedup.app.crypto.KeystoreManager;
import com.showedup.app.data.ShowedUpDatabase;
import com.showedup.app.data.dao.AttendanceDao;
import com.showedup.app.data.dao.DayOffDao;
import com.showedup.app.data.dao.PlannedDayOffDao;
import com.showedup.app.data.dao.SecurityEventDao;
import com.showedup.app.data.dao.TimetableDao;
import com.showedup.app.data.dao.WeeklyScheduleDao;
import com.showedup.app.data.repository.AttendanceRepository;
import com.showedup.app.data.repository.ScheduleRepository;
import com.showedup.app.data.repository.SecurityRepository;
import com.showedup.app.di.AppModule_ProvideAttendanceDaoFactory;
import com.showedup.app.di.AppModule_ProvideDatabaseFactory;
import com.showedup.app.di.AppModule_ProvideDayOffDaoFactory;
import com.showedup.app.di.AppModule_ProvidePlannedDayOffDaoFactory;
import com.showedup.app.di.AppModule_ProvideSecurityEventDaoFactory;
import com.showedup.app.di.AppModule_ProvideTimetableDaoFactory;
import com.showedup.app.di.AppModule_ProvideWeeklyScheduleDaoFactory;
import com.showedup.app.scheduler.BootReceiver;
import com.showedup.app.scheduler.BootReceiver_MembersInjector;
import com.showedup.app.signal.MultiModalSignalCollector;
import com.showedup.app.signal.service.SignalCollectionService;
import com.showedup.app.signal.service.SignalCollectionService_MembersInjector;
import com.showedup.app.ui.screens.calendar.CalendarViewModel;
import com.showedup.app.ui.screens.calendar.CalendarViewModel_HiltModules;
import com.showedup.app.ui.screens.calendar.CalendarViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.showedup.app.ui.screens.calendar.CalendarViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.showedup.app.ui.screens.dayoff.DayOffViewModel;
import com.showedup.app.ui.screens.dayoff.DayOffViewModel_HiltModules;
import com.showedup.app.ui.screens.dayoff.DayOffViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.showedup.app.ui.screens.dayoff.DayOffViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.showedup.app.ui.screens.export.ExportViewModel;
import com.showedup.app.ui.screens.export.ExportViewModel_HiltModules;
import com.showedup.app.ui.screens.export.ExportViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.showedup.app.ui.screens.export.ExportViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.showedup.app.ui.screens.home.HomeViewModel;
import com.showedup.app.ui.screens.home.HomeViewModel_HiltModules;
import com.showedup.app.ui.screens.home.HomeViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.showedup.app.ui.screens.home.HomeViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.showedup.app.ui.screens.log.AttendanceLogViewModel;
import com.showedup.app.ui.screens.log.AttendanceLogViewModel_HiltModules;
import com.showedup.app.ui.screens.log.AttendanceLogViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.showedup.app.ui.screens.log.AttendanceLogViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.showedup.app.ui.screens.schedule.ScheduleViewModel;
import com.showedup.app.ui.screens.schedule.ScheduleViewModel_HiltModules;
import com.showedup.app.ui.screens.schedule.ScheduleViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.showedup.app.ui.screens.schedule.ScheduleViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerShowedUpApplication_HiltComponents_SingletonC {
  private DaggerShowedUpApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public ShowedUpApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements ShowedUpApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public ShowedUpApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements ShowedUpApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public ShowedUpApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements ShowedUpApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public ShowedUpApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements ShowedUpApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public ShowedUpApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements ShowedUpApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public ShowedUpApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements ShowedUpApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public ShowedUpApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements ShowedUpApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public ShowedUpApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends ShowedUpApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends ShowedUpApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends ShowedUpApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends ShowedUpApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(6).put(AttendanceLogViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, AttendanceLogViewModel_HiltModules.KeyModule.provide()).put(CalendarViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, CalendarViewModel_HiltModules.KeyModule.provide()).put(DayOffViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, DayOffViewModel_HiltModules.KeyModule.provide()).put(ExportViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, ExportViewModel_HiltModules.KeyModule.provide()).put(HomeViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, HomeViewModel_HiltModules.KeyModule.provide()).put(ScheduleViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, ScheduleViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }
  }

  private static final class ViewModelCImpl extends ShowedUpApplication_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AttendanceLogViewModel> attendanceLogViewModelProvider;

    private Provider<CalendarViewModel> calendarViewModelProvider;

    private Provider<DayOffViewModel> dayOffViewModelProvider;

    private Provider<ExportViewModel> exportViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<ScheduleViewModel> scheduleViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.attendanceLogViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.calendarViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.dayOffViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.exportViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.scheduleViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(6).put(AttendanceLogViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) attendanceLogViewModelProvider)).put(CalendarViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) calendarViewModelProvider)).put(DayOffViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) dayOffViewModelProvider)).put(ExportViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) exportViewModelProvider)).put(HomeViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) homeViewModelProvider)).put(ScheduleViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) scheduleViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.showedup.app.ui.screens.log.AttendanceLogViewModel 
          return (T) new AttendanceLogViewModel(singletonCImpl.attendanceRepositoryProvider.get());

          case 1: // com.showedup.app.ui.screens.calendar.CalendarViewModel 
          return (T) new CalendarViewModel(singletonCImpl.attendanceRepositoryProvider.get());

          case 2: // com.showedup.app.ui.screens.dayoff.DayOffViewModel 
          return (T) new DayOffViewModel(singletonCImpl.attendanceRepositoryProvider.get(), singletonCImpl.scheduleRepositoryProvider.get());

          case 3: // com.showedup.app.ui.screens.export.ExportViewModel 
          return (T) new ExportViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.attendanceRepositoryProvider.get());

          case 4: // com.showedup.app.ui.screens.home.HomeViewModel 
          return (T) new HomeViewModel(singletonCImpl.scheduleRepositoryProvider.get(), singletonCImpl.attendanceRepositoryProvider.get());

          case 5: // com.showedup.app.ui.screens.schedule.ScheduleViewModel 
          return (T) new ScheduleViewModel(singletonCImpl.scheduleRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends ShowedUpApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends ShowedUpApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }

    @Override
    public void injectSignalCollectionService(SignalCollectionService signalCollectionService) {
      injectSignalCollectionService2(signalCollectionService);
    }

    private SignalCollectionService injectSignalCollectionService2(
        SignalCollectionService instance) {
      SignalCollectionService_MembersInjector.injectSignalCollector(instance, singletonCImpl.multiModalSignalCollectorProvider.get());
      SignalCollectionService_MembersInjector.injectAttendanceRepository(instance, singletonCImpl.attendanceRepositoryProvider.get());
      SignalCollectionService_MembersInjector.injectSecurityRepository(instance, singletonCImpl.securityRepositoryProvider.get());
      return instance;
    }
  }

  private static final class SingletonCImpl extends ShowedUpApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<ShowedUpDatabase> provideDatabaseProvider;

    private Provider<HashUtils> hashUtilsProvider;

    private Provider<CanonicalJson> canonicalJsonProvider;

    private Provider<KeystoreManager> keystoreManagerProvider;

    private Provider<AttendanceRepository> attendanceRepositoryProvider;

    private Provider<ScheduleRepository> scheduleRepositoryProvider;

    private Provider<MultiModalSignalCollector> multiModalSignalCollectorProvider;

    private Provider<SecurityRepository> securityRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private TimetableDao timetableDao() {
      return AppModule_ProvideTimetableDaoFactory.provideTimetableDao(provideDatabaseProvider.get());
    }

    private AttendanceDao attendanceDao() {
      return AppModule_ProvideAttendanceDaoFactory.provideAttendanceDao(provideDatabaseProvider.get());
    }

    private DayOffDao dayOffDao() {
      return AppModule_ProvideDayOffDaoFactory.provideDayOffDao(provideDatabaseProvider.get());
    }

    private PlannedDayOffDao plannedDayOffDao() {
      return AppModule_ProvidePlannedDayOffDaoFactory.providePlannedDayOffDao(provideDatabaseProvider.get());
    }

    private WeeklyScheduleDao weeklyScheduleDao() {
      return AppModule_ProvideWeeklyScheduleDaoFactory.provideWeeklyScheduleDao(provideDatabaseProvider.get());
    }

    private SecurityEventDao securityEventDao() {
      return AppModule_ProvideSecurityEventDaoFactory.provideSecurityEventDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<ShowedUpDatabase>(singletonCImpl, 0));
      this.hashUtilsProvider = DoubleCheck.provider(new SwitchingProvider<HashUtils>(singletonCImpl, 2));
      this.canonicalJsonProvider = DoubleCheck.provider(new SwitchingProvider<CanonicalJson>(singletonCImpl, 3));
      this.keystoreManagerProvider = DoubleCheck.provider(new SwitchingProvider<KeystoreManager>(singletonCImpl, 4));
      this.attendanceRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AttendanceRepository>(singletonCImpl, 1));
      this.scheduleRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ScheduleRepository>(singletonCImpl, 5));
      this.multiModalSignalCollectorProvider = DoubleCheck.provider(new SwitchingProvider<MultiModalSignalCollector>(singletonCImpl, 6));
      this.securityRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<SecurityRepository>(singletonCImpl, 7));
    }

    @Override
    public void injectShowedUpApplication(ShowedUpApplication showedUpApplication) {
    }

    @Override
    public void injectBootReceiver(BootReceiver bootReceiver) {
      injectBootReceiver2(bootReceiver);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private BootReceiver injectBootReceiver2(BootReceiver instance) {
      BootReceiver_MembersInjector.injectTimetableDao(instance, timetableDao());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.showedup.app.data.ShowedUpDatabase 
          return (T) AppModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 1: // com.showedup.app.data.repository.AttendanceRepository 
          return (T) new AttendanceRepository(singletonCImpl.attendanceDao(), singletonCImpl.dayOffDao(), singletonCImpl.plannedDayOffDao(), singletonCImpl.hashUtilsProvider.get(), singletonCImpl.canonicalJsonProvider.get(), singletonCImpl.keystoreManagerProvider.get());

          case 2: // com.showedup.app.crypto.HashUtils 
          return (T) new HashUtils();

          case 3: // com.showedup.app.crypto.CanonicalJson 
          return (T) new CanonicalJson();

          case 4: // com.showedup.app.crypto.KeystoreManager 
          return (T) new KeystoreManager();

          case 5: // com.showedup.app.data.repository.ScheduleRepository 
          return (T) new ScheduleRepository(singletonCImpl.timetableDao(), singletonCImpl.weeklyScheduleDao(), singletonCImpl.hashUtilsProvider.get(), singletonCImpl.canonicalJsonProvider.get());

          case 6: // com.showedup.app.signal.MultiModalSignalCollector 
          return (T) new MultiModalSignalCollector(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.hashUtilsProvider.get());

          case 7: // com.showedup.app.data.repository.SecurityRepository 
          return (T) new SecurityRepository(singletonCImpl.securityEventDao(), singletonCImpl.hashUtilsProvider.get(), singletonCImpl.canonicalJsonProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
