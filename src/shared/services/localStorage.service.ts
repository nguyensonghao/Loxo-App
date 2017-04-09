import { Injectable } from '@angular/core';

@Injectable()
export class LocalStorageService {	
	constructor () {
		
	}	

    setItem (key: string, value: any) {            
        localStorage.setItem(key, JSON.stringify(value));                                
    }

    getItem (key: string): any {                
        let result = localStorage.getItem(key);
        return result ? JSON.parse(result) :null;
    }

    deleteItem (key: string) {                        
        localStorage.removeItem(key);                        
    }

    clear () {
        localStorage.clear();            
    }	
}