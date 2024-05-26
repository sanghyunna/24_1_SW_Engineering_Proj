import { baseURL } from "@/lib/constants";

export default async function ({
	params,
}: {
	params: { projectId: string; issueId: string };
}) {
	//const project = await fetch(baseURL + '/project/' + params.projectId + '/issue/' + params.issueId).then(res => res.json());

	const issue = {
		createDate: "2023-05-18T12:00:00",
		updateDate: "2023-05-18T12:00:00",
		id: 1,
		title: "Issue 1",
		dueDate: "2023-05-25T12:00:00",
		content: "This is issue 1",
		reporter: {
			id: 1,
			name: "john",
		},
		assignee: [
			{
				id: 4,
				name: "alice",
			},
		],
		fixer: {
			id: 4,
			name: "alice",
		},
		status: "NEW",
		priority: "MAJOR",
	};

	const comments = {
		createDate: "2023-05-18T14:00:00",
		updateDate: "2023-05-18T14:00:00",
		id: 1,
		content: "This is a comment for issue 1",
		issue: {
			createDate: "2023-05-18T12:00:00",
			updateDate: "2023-05-18T12:00:00",
			id: 1,
			title: "Issue 1",
			dueDate: "2023-05-25T12:00:00",
			content: "This is issue 1",
			reporter: {
				id: 1,
				name: "john",
			},
			assignee: [
				{
					id: 4,
					name: "alice",
				},
			],
			fixer: {
				id: 4,
				name: "alice",
			},
			status: "NEW",
			priority: "MAJOR",
		},
		commentOwner: {
			id: 3,
			name: "bob",
		},
	};

	return (
		<div>
			<div className="px-6 py-5 border-b border-gray-200 dark:border-gray-800">
				<h1 className="text-2xl font-bold">{issue.title}</h1>
			</div>
			<div className="px-6 py-5 grid grid-cols-2 gap-4">
				<div>
					<p className="text-gray-500 dark:text-gray-400 text-sm">Created</p>
					<p className="text-gray-900 font-medium">{issue.createDate}</p>
				</div>
				<div>
					<p className="text-gray-500 dark:text-gray-400 text-sm">
						Last Updated
					</p>
					<p className="text-gray-900 font-medium">{issue.updateDate}</p>
				</div>
				<div>
					<p className="text-gray-500 dark:text-gray-400 text-sm">Reporter</p>
					<p className="text-gray-900 font-medium">{issue.reporter.name}</p>
				</div>
				<div>
					<p className="text-gray-500 dark:text-gray-400 text-sm">Assignee</p>
					<p className="text-gray-900 font-medium">
						{issue.assignee.map((item) => item.name).join(", ")}
					</p>
				</div>
				<div>
					<p className="text-gray-500 dark:text-gray-400 text-sm">Status</p>
					<p className="text-gray-900 font-medium">{issue.status}</p>
				</div>
				<div>
					<p className="text-gray-500 dark:text-gray-400 text-sm">Priority</p>
					<p className="text-gray-900 font-medium">{issue.priority}</p>
				</div>
			</div>

			<div className="my-12">{issue.content}</div>
			<div>Comments...</div>
		</div>
	);
}
