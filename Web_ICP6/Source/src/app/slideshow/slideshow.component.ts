import { Component} from '@angular/core';

@Component({
  selector: 'app-slideshow',
  templateUrl: './slideshow.component.html',
  styleUrls: ['./slideshow.component.css']
})
export class SlideshowComponent {
  images = [
    'https://images.unsplash.com/photo-1504674900247-0877df9cc836?ixlib=rb-1.2.1\n'   +
  '        &ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=730&q=80',
    'https://images.unsplash.com/photo-1499028344343-cd173ffc68a9?ixlib=rb-1.2.1\n' +
  '        &ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=730&q=80',
    'https://images.unsplash.com/photo-1484659619207-9165d119dafe?ixlib=rb-1.2.1\n' +
  '        &ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=730&q=80'
  ];

  imageObject = [{
    image: this.images[0],
    thumbImage: this.images[0]
  }, {
    image: this.images[1],
    thumbImage: this.images[1]
  }, {
    image: this.images[2],
    thumbImage: this.images[2]
  }];

}
