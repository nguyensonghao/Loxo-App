import { Face } from './face.model';

export class Box {
	public id: number;
	public box: Face;
	public type: string;
	public correctAnswer: string;

	constructor (id: number, box: Face, correctAnswer: string, type: string) {
		this.id = id;
		this.box = box;
		this.correctAnswer = correctAnswer;
		this.type = type;
	}
}