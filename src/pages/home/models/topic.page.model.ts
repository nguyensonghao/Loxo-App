export class TopicPageModel {
	id: number;
	title: string;
	page: any;
	topicId: string;

	constructor () {
		this.id = -1;
		this.title = "";
		this.page = null;
		this.topicId = "";
	}
}