import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';

interface VenueSearchResponse {
  meta: {};
  response: {
    venues: Venue[];
    confident: boolean;
  };
}

interface Venue {
  id: string;
  name: string;
  location: {
    address: string;
    postalCode: string;
    city: string;
    state: string;
    country: string;
    formattedAddress: string[];
  };
  categories: {
    name: string;
  };
  referralId: string;
  hasPerk: boolean;
}

@Component({
  selector: 'app-search-venue',
  templateUrl: './search-venue.component.html',
  styleUrls: ['./search-venue.component.css']
})
export class SearchVenueComponent implements OnInit {
  @ViewChild('place') placesRef: ElementRef;
  placeValue: any;
  venueList: Venue[] = [];

  currentLat: any;
  currentLong: any;
  geolocationPosition: any;

  isLoading = true;
  places: Venue[];

  constructor(private _http: HttpClient) {
  }

  ngOnInit() {
    window.navigator.geolocation.getCurrentPosition(
      position => {
        this.geolocationPosition = position;
        this.currentLat = position.coords.latitude;
        this.currentLong = position.coords.longitude;
      });
  }

  // calls the API
  getVenues() {
    this.placeValue = this.placesRef.nativeElement.value;

    if (this.placeValue) {
      this._http.get(`https://api.foursquare.com/v2/venues/search?client_id={}` + `
      &client_secret={}`,
        {params: {
          ll: `${this.currentLat},${this.currentLong}`,
            query: this.placeValue
          }
        })
        .subscribe(({response}: VenueSearchResponse) => {
          this.isLoading = false;
          this.venueList = response.venues;
        });
    }
  }
}
