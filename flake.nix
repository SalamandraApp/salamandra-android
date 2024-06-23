{
  description = "Salamandra Server";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils, ... }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
        nodePackages = pkgs.callPackage ./node-env/default.nix {};
      in {
        devShells.default = pkgs.mkShell {
          nativeBuildInputs = with pkgs; [
            awscli2
            nodePackages."@aws-amplify/cli"
            pkgs.buildFHSUserEnv
          ];

          shellHook = ''
            echo -e "Entering \e[1;31mSALAMANDRA-SERVER SHELL\e[0m"
            echo -e " - AWS CLI"
            echo -e " - AWS Amplify CLI"
          '';
        };

        apps = {
          amplify = pkgs.buildFHSUserEnv {
            name = "amplify-env";
            targetPkgs = pkgs: [
              pkgs.awscli2
              pkgs.nodejs
              pkgs.glibc
              pkgs.libstdcxx
              nodePackages."@aws-amplify/cli"
            ];
            runScript = "${nodePackages."@aws-amplify/cli"}/bin/amplify";
          };
        };
      });
}

