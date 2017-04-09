import { Pipe, PipeTransform } from '@angular/core';
declare var LZString: any;

@Pipe({name: 'decode'})
export class DecodePipe implements PipeTransform {
  	transform(value: string): string {
		value = value ? value : '';
  		let decodeValue = value.replace("@cp@", "");
  		return LZString.decompressFromBase64(decodeValue);
  	}
}