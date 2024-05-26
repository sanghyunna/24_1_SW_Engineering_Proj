import { ProjectBox } from "@/components/projectBox";
import Link from "next/link";

export default function Page() {
	//const project = await fetch(baseURL + '/project').then(res => res.json());

	return (
		<div className="h-screen">
			<div className="h-2/5 p-24 flex flex-col justify-end">
				<div className="text-8xl font-thin">IssueTracker</div>
			</div>
			<div className="h-3/5 bg-gray-100 p-24">
				<div className="text-7xl pb-16 font-semibold">Welcome.</div>
				<div className="flex overflow-auto pb-4">
					<Link href={"/projects/1"}>
						<ProjectBox />
					</Link>
					<ProjectBox />
					<ProjectBox />
					<ProjectBox />
				</div>
			</div>
		</div>
	);
}
