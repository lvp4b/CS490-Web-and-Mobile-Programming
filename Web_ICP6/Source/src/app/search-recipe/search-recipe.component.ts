import {Component, ElementRef, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';

interface RecipeSearchResponse {
  count: number;
  from: number;
  hits: Hit[];
  more: boolean;
  q: string;
  to: number;
}

interface Hit {
  recipe: Recipe;
}

interface Recipe {
  calories: number;
  dietLabels: string[];
  healthLabels: string[];
  image: string | null;
  ingredientLines: string[];
  ingredients: {
    image: string | null;
    text: string;
    weight: number;
  }[];
  label: string;
  source: string;
  totalTime: number;
  uri: string;
  url: string;
  yield: number;
}

@Component({
  selector: 'app-search-recipe',
  templateUrl: './search-recipe.component.html',
  styleUrls: ['./search-recipe.component.css']
})
export class SearchRecipeComponent {
  @ViewChild('recipe') recipesRef: ElementRef;
  @ViewChild('place') placesRef: ElementRef;
  recipeValue: any;
  recipeList = [];

  isLoading = true;

  recipes: Recipe[];

  constructor(private _http: HttpClient) {
  }

  // calls the API
  getRecipes() {

    this.recipeValue = this.recipesRef.nativeElement.value;

    if (this.recipeValue) {
      this._http.get(`https://api.edamam.com/search?app_id={}` +
        `&app_key={}`,
        {params: {
          q: this.recipeValue
        }})
        .subscribe(({hits}: RecipeSearchResponse) => {
          this.isLoading = false;
          this.recipes = hits.map(hit => hit.recipe);
        });
    }
  }
}
