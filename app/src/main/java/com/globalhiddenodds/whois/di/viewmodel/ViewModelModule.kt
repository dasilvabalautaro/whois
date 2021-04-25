package com.globalhiddenodds.whois.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.globalhiddenodds.whois.presentation.presenter.GetDataPersonCloudViewModel
import com.globalhiddenodds.whois.presentation.presenter.GetFacesByStateViewModel
import com.globalhiddenodds.whois.presentation.presenter.InsertFacesViewModel
import com.globalhiddenodds.whois.presentation.presenter.UpdateDataFaceViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory:
                                               ViewModelFactory):
            ViewModelProvider.Factory
    @Binds
    @IntoMap
    @ViewModelKey(InsertFacesViewModel::class)
    abstract fun bindsInsertFacesViewModel(insertFacesViewModel:
                                               InsertFacesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GetFacesByStateViewModel::class)
    abstract fun bindsGetFacesByStateViewModel(getFacesByStateViewModel:
                                                   GetFacesByStateViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GetDataPersonCloudViewModel::class)
    abstract fun bindsGetDataPersonCloudViewModel(getDataPersonCloudViewModel:
                                                   GetDataPersonCloudViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UpdateDataFaceViewModel::class)
    abstract fun bindsUpdateDataFaceViewModel(updateDataFaceViewModel:
                                              UpdateDataFaceViewModel): ViewModel


}