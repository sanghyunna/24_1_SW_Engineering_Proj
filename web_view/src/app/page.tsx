"use client";

import { ProjectBox } from "@/components/projectBox";
import { baseURL } from "@/lib/constants";
import { Project } from "@/lib/types";
import { useEffect, useState } from "react";

export default function Page() {
	const [projects, setProjects] = useState<Project[]>([]);
	const [loading, setLoading] = useState(true);

	useEffect(() => {
		setLoading(true);
		const fetchData = async () => {
			try {
				const projectsRes = await fetch(`${baseURL}/project`).then((res) =>
					res.json()
				);
				setProjects(projectsRes);
			} catch (err) {
				console.log(err);
			} finally {
				setLoading(false);
			}
		};
		fetchData();
	}, []);

	return (
		<div className="h-screen">
			<div className="h-2/5 p-24 flex flex-col justify-end">
				<div className="text-8xl font-thin">IssueTracker</div>
			</div>
			<div className="h-3/5 bg-gray-100 p-24">
				<div className="text-7xl pb-16 font-semibold">Welcome.</div>
				<div className="flex overflow-auto pb-4">
					{loading && <div>Loading...</div>}
					{!loading &&
						projects.map((project) => (
							<ProjectBox key={project.id} data={project} />
						))}
				</div>
			</div>
		</div>
	);
}
