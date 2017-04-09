export class Face {
	public id: number;
	public text: string;
	public sound: string;
	public image: string;
	public hint: string;
	public language: number;

	constructor (id: number, text: string, sound: string, image: string, hint: string, language: number) {
		this.id = id;
		this.text = text;
		this.sound = sound;
		this.image = image;
		this.hint = hint;
		this.language = language;
	}
}