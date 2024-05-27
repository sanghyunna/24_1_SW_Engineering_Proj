import { Project } from "@/lib/types";
import Link from "next/link";

export function ProjectBox({ data }: { data: Project }) {
	return (
		<Link href={"/projects/" + data.id}>
			<div className="p-8 mx-4 w-[24rem] h-[10rem] bg-white border-b-4 border-blue-600 flex flex-col justify-between flex-shrink-0">
				<div className="font-semibold text-2xl">{data.title}</div>
				<div className="font-light text-right">{data.projectOwner}</div>
			</div>
		</Link>
	);
}
