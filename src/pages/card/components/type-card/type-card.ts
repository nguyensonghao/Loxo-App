import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

@Component({
    selector: 'type-card',
    templateUrl: 'type-card.html',
})
export class TypeCard {  
    @Input() type:number;
    @Input() size: number;
    @Output() changeType = new EventEmitter();
    @Output() goLearn = new EventEmitter();

    constructor (public navController: NavController, public navParams: NavParams) {

    }

    change (type: number) {
        this.changeType.emit(type);
    }

    startGame () {
    	this.goLearn.emit();
    }
}
