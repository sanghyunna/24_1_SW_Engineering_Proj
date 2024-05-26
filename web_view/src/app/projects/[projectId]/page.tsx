import { IssueBox } from "@/components/issueBox";
import { baseURL } from "@/lib/constants";

export default async function ({ params }: { params: { projectId: string } }) {
	//const project = await fetch(baseURL + '/project/' + params.projectId).then(res => res.json());
	const project = {
		title: "title1",
		projectOwner: "Temple",
		createDate: "2023-05-18T12:00:00",
		updateDate: "2023-05-18T12:00:00",
	};

	return (
		<div>
			<div className="px-6 py-5 border-b border-gray-200 dark:border-gray-800">
				<h1 className="text-2xl font-bold">{project.title}</h1>
			</div>
			<div className="px-6 py-5 grid grid-cols-2 gap-4">
				<div>
					<p className="text-gray-500 dark:text-gray-400 text-sm">Created</p>
					<p className="text-gray-900 font-medium">{project.createDate}</p>
				</div>
				<div>
					<p className="text-gray-500 dark:text-gray-400 text-sm">
						Last Updated
					</p>
					<p className="text-gray-900 font-medium">{project.updateDate}</p>
				</div>
				<div>
					<p className="text-gray-500 dark:text-gray-400 text-sm">Owner</p>
					<div className="flex items-center gap-2">
						<p className="text-gray-900 font-medium">{project.projectOwner}</p>
					</div>
				</div>
			</div>

			<div className="flex flex-col place-items-center pt-12">
				<IssueBox />
				<IssueBox />
				<IssueBox />
			</div>
		</div>
	);
}
