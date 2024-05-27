export interface Project {
	id: number;
	createDate: string;
	updateDate: string;
	title: string;
	projectOwner: string;
	pl: string[];
	dev: string[];
	tester: string[];
}

export interface Issue {
	id: number;
	createDate: string;
	updateDate: string;
	title: string;
	dueDate: string;
	content: string;
	projectId: number;
	reporter: string;
	assignee: string[];
	fixer: string | null;
	status: string;
	priority: string;
}

export interface Comment {
	id: number;
	createDate: string;
	updateDate: string;
	content: string;
	issueId: number;
	commentOwner: string;
}

interface StatusCount {
	NEW: number;
	CLOSED: number;
	ASSIGNED: number;
	FIXED: number;
	REOPENED: number;
}

interface PriorityCount {
	MAJOR: number;
	MINOR: number;
	TRIVIAL: number;
	BLOCKER: number;
	CRITICAL: number;
}

export interface IssueStats {
	todayCount: number;
	monthlyCount: number;
	statusCount: StatusCount;
	priorityCount: PriorityCount;
}
