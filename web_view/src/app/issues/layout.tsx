import { ContentBg } from "@/components/contentBg";
import { Navbar } from "@/components/navbar";

export default function ({ children }: { children: React.ReactNode }) {
	return (
		<>
			<Navbar />
			<div className="flex flex-col place-items-center">
				<ContentBg>{children}</ContentBg>
			</div>
		</>
	);
}
