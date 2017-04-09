export class Rank {
	public id: number;
	public userId: number;
	public userName: String;
	public level: number;
	public exp: number;
	public avatar: String;

	constructor () {
		this.id = 0;
		this.userId = 0;
		this.userName = "";
		this.level = 0;
		this.exp = 0;
		this.avatar = "";
	}
}