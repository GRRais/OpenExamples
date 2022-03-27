package ru.rayanis.dagger2

import dagger.Component
import dagger.Module
import dagger.Provides

@Component(modules = [AppModule::class])
interface AppComponent {
    val computer: Computer
    fun inject(activity: MainActivity)
}

@Module
object AppModule {

    @Provides
    fun provideComputer(
        processor: Processor ,
        ram: RAM ,
        motherboard: Motherboard
    ): Computer {
        return Computer(
            processor = processor,
            ram = ram,
            motherboard = motherboard
        )
    }

    @Provides
    fun provideProcessor(): Processor {
        return Processor()
    }

    @Provides
    fun provideRam(): RAM {
        return RAM()
    }

    @Provides
    fun provideMotherboard(): Motherboard {
        return Motherboard()
    }
}