import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NgImageSliderModule} from 'ng-image-slider';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { HomeComponent } from './home/home.component';
import { SearchRecipeComponent } from './search-recipe/search-recipe.component';
import { SearchVenueComponent } from './search-venue/search-venue.component' ;
import { HttpClientModule } from '@angular/common/http';
import { SlideshowComponent } from './slideshow/slideshow.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    HomeComponent,
    SearchRecipeComponent,
    SearchVenueComponent,
    SlideshowComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    NgImageSliderModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
