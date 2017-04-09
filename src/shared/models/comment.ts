export class Comment {
	public id: number;
	public username: String;
	public date: String;
	public like: number;
	public avatar: String;
	public parentId: number;

	constructor () {
		this.id = 0;
		this.username = "";
		this.date = "";
		this.like = 0;
		this.avatar = "";
		this.parentId = 0;
	}
}