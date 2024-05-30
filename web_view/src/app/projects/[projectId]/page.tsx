"use client";

import { IssueBox } from "@/components/issueBox";
import { CreateIssueModal } from "@/components/modal/createIssueModal";
import { UpdateProjectModal } from "@/components/modal/updateProjectModal";
import { SearchBox } from "@/components/searchBox";
import { StatsBox } from "@/components/statsBox";
import { baseURL } from "@/lib/constants";
import { Issue, IssueStats, Project } from "@/lib/types";
import { useEffect, useState } from "react";

export default function ({ params }: { params: { projectId: string } }) {
	const { projectId } = params;
	const [project, setProject] = useState<Project>({} as Project);
	const [stats, setStats] = useState<IssueStats>({} as IssueStats);
	const [issues, setIssues] = useState<Issue[]>([]);
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState<string | null>(null);

	useEffect(() => {
		const fetchData = async () => {
			setLoading(true);
			try {
				const projectRes = await fetch(`${baseURL}/project/${projectId}`).then(
					(res) => res.json()
				);
				setProject(projectRes);

				const statsRes = await fetch(
					`${baseURL}/project/${projectId}/issue/stats`
				).then((res) => res.json());
				setStats(statsRes);

				const issuesRes = await fetch(
					`${baseURL}/project/${projectId}/issue`
				).then((res) => res.json());
				setIssues(issuesRes);
			} catch (err) {
				setError("Failed to fetch data");
			} finally {
				setLoading(false);
			}
		};
		fetchData();
	}, []);

	if (loading) return <div>Loading...</div>;
	if (error) return <div>{error}</div>;

	return (
		<div>
			<div>
				<div className="pb-12">
					<div className="text-base text-gray-400">Project</div>
					<div className="flex justify-between">
						<div className="text-5xl font-bold">{project.title}</div>
						<UpdateProjectModal project={project} />
					</div>
				</div>

				<div className="px-6 py-5 grid grid-cols-2 gap-4 rounded-lg border">
					<div>
						<p className="text-gray-500 dark:text-gray-400 text-sm">Created</p>
						<p className="text-gray-900 font-medium">
							{new Date(project.createDate).toLocaleString()}
						</p>
					</div>
					<div>
						<p className="text-gray-500 dark:text-gray-400 text-sm">
							Last Updated
						</p>
						<p className="text-gray-900 font-medium">
							{new Date(project.updateDate).toLocaleString()}
						</p>
					</div>
					<div>
						<p className="text-gray-500 dark:text-gray-400 text-sm">Owner</p>
						<div className="flex items-center gap-2">
							<p className="text-gray-900 font-medium">
								{project.projectOwner}
							</p>
						</div>
					</div>
					<div>
						<p className="text-gray-500 dark:text-gray-400 text-sm">PL</p>
						<div className="flex items-center gap-2">
							<p className="text-gray-900 font-medium">
								{project.pl.join(", ")}
							</p>
						</div>
					</div>
					<div>
						<p className="text-gray-500 dark:text-gray-400 text-sm">Dev</p>
						<div className="flex items-center gap-2">
							<p className="text-gray-900 font-medium">
								{project.dev.join(", ")}
							</p>
						</div>
					</div>
					<div>
						<p className="text-gray-500 dark:text-gray-400 text-sm">Tester</p>
						<div className="flex items-center gap-2">
							<p className="text-gray-900 font-medium">
								{project.tester.join(", ")}
							</p>
						</div>
					</div>
				</div>
			</div>

			<StatsBox data={stats} />

			<SearchBox projectId={project.id} setData={setIssues} />

			<CreateIssueModal projectId={project.id} />

			<div className="flex flex-col place-items-center pt-4 mb-96">
				{issues.map((issue) => (
					<IssueBox key={issue.id} data={issue} />
				))}
			</div>
		</div>
	);
}
